import * as Router from 'koa-router';
import { Connection } from 'typeorm';
import * as _ from 'lodash';

import { Fridge } from '../db/entity/Fridge';
import { Controller } from './Controller';
import FridgeValidation from './FridgeValidation';
import ValidationMiddleware from '../middleware/ValidationMiddleware';
import { Quantity } from '../db/entity/Quantity';
import { Ingredient } from '../db/entity/Ingredient';
import { QueryExpressionMap } from 'typeorm/query-builder/QueryExpressionMap';

export class FridgeController extends Controller {
    private dbConnection: Connection;

    constructor(router: Router, connection: Connection) {
        super(router);
        this.dbConnection = connection; // Hang onto a connection to the DB
    }

    setupRoutes(): void {
        this.router.get('/fridges', async (ctx) => {
            let fridge = await this.dbConnection.getRepository(Fridge).find({ order: { id: "ASC" }});

            ctx.body = { data: fridge };
        });

        this.router.post('/fridges', ValidationMiddleware(FridgeValidation.FridgeCreation.body), async (ctx) => {
            let fridge: Fridge = ctx.request.body;

            let newFridge = await this.dbConnection.manager.save(Fridge, fridge);

            ctx.status = 201;
            ctx.body = { id: newFridge.id };
        })

        this.router.put('/fridges/:id/quantities', async (ctx) => {
            let quantity: Quantity = Object.assign(new Quantity, ctx.request.body);

            let ingredientPromise = this.dbConnection.getRepository(Ingredient).findOneById(quantity.ingredient.id);
            let fridgePromise = this.dbConnection.getRepository(Fridge).findOneById(ctx.params.id);
            let [ingredient, fridge] = await Promise.all([ingredientPromise, fridgePromise]);

            if (!fridge || !ingredient) {
                // Return error body
                ctx.status = 400;
            } else {
                let existingQuantity = _.find(fridge.quantities, q => q.ingredient.id === quantity.ingredient.id);
                let newQuantity = (existingQuantity) ? existingQuantity.add(quantity) : quantity;
                let savedQuantity = await this.dbConnection.getRepository(Quantity).save(newQuantity);

                if (!existingQuantity) {
                    fridge.quantities = fridge.quantities.concat(newQuantity);
                    await this.dbConnection.getRepository(Fridge).save(fridge);
                }

                ctx.body = { id: ctx.params.id };
                ctx.status = 200;
            }
        });

        this.router.get('/fridges/:id', async (ctx) => {
            let fridge = await this.dbConnection.getRepository(Fridge).findOneById(ctx.params.id);
            
            if (fridge) {
                ctx.status = 200;
                ctx.body = { data: fridge };
            } else {
                ctx.status = 404;
            }
        });
    }
}