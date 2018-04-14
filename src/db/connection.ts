import 'reflect-metadata';
import {createConnection, Connection} from 'typeorm';
import {Ingredient} from './entity/Ingredient';
import { Quantity } from './entity/Quantity';
import { Fridge } from './entity/Fridge';

export class Db {
    async connect(): Promise<Connection> {
        return createConnection({
            type: 'postgres',
            host: 'localhost',
            port: 5432,
            username: 'fridge',
            password: 'fridge',
            database: 'fridge',
            entities: [
                Ingredient,
                Quantity,
                Fridge
            ],
            synchronize: true,
            logging: true
        });
    }
}