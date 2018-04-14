import * as Router from 'koa-router';
import { Connection } from 'typeorm';

import { Fridge } from '../db/entity/Fridge';
import { Controller } from './Controller';
import FridgeValidation from './FridgeValidation';
import ValidationMiddleware from '../middleware/ValidationMiddleware';

export class FridgeController extends Controller {
    private dbConnection: Connection;

    constructor(router: Router, connection: Connection) {
        super(router);
        this.dbConnection = connection; // Hang onto a connection to the DB
    }

    setupRoutes(): void {
        this.router.get('/fridges', async (ctx) => {
            let fridge = await this.dbConnection.getRepository(Fridge).find();

            ctx.body = { data: fridge };
        });

        this.router.post('/fridges', ValidationMiddleware(FridgeValidation.FridgeCreation.body), async (ctx) => {
            let fridge: Fridge = ctx.request.body;

            let newFridge = await this.dbConnection.manager.save(Fridge, fridge);

            ctx.status = 201;
            ctx.body = { id: newFridge.id };
        })

        this.router.get('/fridges/:id', async (ctx) => {
            let fridge = await this.dbConnection.getRepository(Fridge).findOneById(ctx.params.id);
            
            ctx.body = { data: fridge };
        });
    }
}