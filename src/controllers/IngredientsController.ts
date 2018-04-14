import * as Router from 'koa-router';
import { Connection } from 'typeorm';

import { Ingredient } from '../db/entity/Ingredient';
import { Controller } from './Controller';

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
    }
}