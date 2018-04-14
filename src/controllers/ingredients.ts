import * as Router from 'koa-router';
import { Connection } from 'typeorm';
import { Controller } from './controller';
import { Ingredient } from '../db/entity/Ingredient';

export class IngredientsController extends Controller {
    private dbConnection: Connection;

    constructor(router: Router, connection: Connection) {
        super(router);
        this.dbConnection = connection; // Hang onto a connection to the DB
    }

    setupRoutes(): void {
        this.router.get('/ingredients', async (ctx) => {
            let ingredients = await this.dbConnection.manager.find(Ingredient);

            console.log(`All ingredients from the db = ${ingredients}`);

            ctx.body = { data: ingredients };
        });
    }
}