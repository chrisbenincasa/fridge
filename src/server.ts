import * as Koa from 'koa';
import * as Router from 'koa-router';
import {Controllers} from './controllers/main';
import {Middleware} from './middleware/middleware';

function main(port: Number = 3000) {
    const app = new Koa();
    const router = new Router();
    
    Middleware.setupMiddleware(app);
    
    Controllers.getControllers(router).forEach(c => c.setupRoutes());
    
    app.use(router.routes());
    
    app.listen(port);
    
    console.log(`Server running on port ${port}`);
};

main();