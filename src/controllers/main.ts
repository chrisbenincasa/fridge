import * as Router from 'koa-router';
import { Controller } from './controller';


export class MainController extends Controller {
    constructor(router: Router) {
        super(router);
    }

    setupRoutes(): void {
        this.router.get('/*', async (ctx) => {
            ctx.body = {"response": "Hello, World!"};
        });
    }
}

export class Controllers {
    static getControllers(router: Router): Controller[] {
        return [
            new MainController(router)
        ];
    }
}