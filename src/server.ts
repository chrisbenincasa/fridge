import * as Koa from 'koa';
import * as Router from 'koa-router';
import {Controllers} from './controllers/main';
import {Middleware} from './middleware/middleware';
import {Db} from './db/connection';

async function main(port: Number = 3000): Promise<void> {
    const app = new Koa();
    const router = new Router();

    const db = new Db();
    const dbConnection = await db.connect();
    
    Middleware.setupMiddleware(app);
    
    Controllers.getControllers(router, dbConnection).forEach(c => c.setupRoutes());
    
    app.use(router.routes());
    
    app.listen(port);
    
    console.log(`Server running on port ${port}`);
};

main();