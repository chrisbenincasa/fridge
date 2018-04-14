import 'reflect-metadata';
import {createConnection, Connection} from 'typeorm';
import {Ingredient} from './entity/Ingredient';

export class Db {
    async connect(): Promise<Connection> {
        return createConnection({
            type: 'postgres',
            host: 'localhost',
            port: 5432,
            username: 'postgres',
            password: 'mysecretpassword',
            database: 'christianbenincasa',
            entities: [
                Ingredient
            ],
            synchronize: true,
            logging: true
        });
    }
}