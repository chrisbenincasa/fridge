import * as Router from 'koa-router';
import { Connection } from 'typeorm';

import { Ingredient } from '../db/entity/Ingredient';
import { Controller } from './Controller';
import ValidationMiddleware from '../middleware/ValidationMiddleware';
import IngredientValidation from '../validation/IngredientValidation';

export class IngredientsController extends Controller {
    private dbConnection: Connection;

    constructor(router: Router, connection: Connection) {
        super(router);
        this.dbConnection = connection; // Hang onto a connection to the DB
    }

    setupRoutes(): void {
        this.router.get('/ingredients', async (ctx) => {
            let ingredients = await this.dbConnection.getRepository(Ingredient).find();

            ctx.body = { data: ingredients };
        });

        this.router.get('/ingredients/:id', async (ctx) => {
            let ingredient = await this.dbConnection.getRepository(Ingredient).findOneById(ctx.params.id);

            if (ingredient) {
                ctx.status = 200;
                ctx.body = { data: ingredient };
            } else {
                ctx.status = 404;
            }
        })

        this.router.post('/ingredients', ValidationMiddleware(IngredientValidation.IngredientCreation.body), async (ctx) => {
            let ingredient: Ingredient = ctx.request.body;

            let newIngredient = await this.dbConnection.manager.save(Ingredient, ingredient);

            ctx.status = 201;
            ctx.body = { id: newIngredient.id };
        });
    }
}