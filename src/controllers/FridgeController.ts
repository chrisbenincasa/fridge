import * as Router from 'koa-router';
import { Connection } from 'typeorm';
import { Controller } from './Controller';
import { Ingredient } from '../db/entity/Ingredient';
import { Fridge } from '../db/entity/Fridge';
import { Quantity } from '../db/entity/Quantity';

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

        this.router.get('/fridges/:id', async (ctx) => {
            let fridge = await this.dbConnection.getRepository(Fridge).findOneById(ctx.params.id);
            
            ctx.body = { data: fridge };
        });
    }
}