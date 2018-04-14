import * as Router from 'koa-router';
import { Connection } from 'typeorm';

export abstract class Controller {
    protected router: Router;

    constructor(router: Router) {
        this.router = router;
    }

    abstract setupRoutes(): void;
}