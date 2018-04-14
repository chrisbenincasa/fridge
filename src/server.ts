import * as Koa from 'koa';
import * as Router from 'koa-router';
import {Controllers, Controller} from './controllers/controller';
import {Middleware} from './middleware/middleware';
import {Db} from './db/connection';
import { Fridge } from './db/entity/Fridge';
import { Connection } from 'typeorm';
import { IngredientsController } from './controllers/IngredientsController';
import { FridgeController } from './controllers/FridgeController';

class Server {
    async main(port: number = 3000): Promise<void> {
        const app = new Koa();
        const router = new Router();
    
        const db = new Db();
        const dbConnection = await db.connect();
        
        Middleware.setupMiddleware(app);
        
        this.configure(router, dbConnection);
        
        app.use(router.routes());
        
        app.listen(port);
        
        console.log(`Server running on port ${port}`);
    }

    configure(router: Router, connection: Connection): void {
        const controllers = [
            new IngredientsController(router, connection),
            new FridgeController(router, connection)
        ];

        controllers.forEach(controller => controller.setupRoutes());
    }
}

new Server().main();