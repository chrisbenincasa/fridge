"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const Koa = require("koa");
const Router = require("koa-router");
const main_1 = require("./controllers/main");
const middleware_1 = require("./middleware/middleware");
function main(port = 3000) {
    const app = new Koa();
    const router = new Router();
    middleware_1.Middleware.setupMiddleware(app);
    main_1.Controllers.getControllers(router).forEach(c => c.setupRoutes());
    app.use(router.routes());
    app.listen(port);
    console.log(`Server running on port ${port}`);
}
;
main();
//# sourceMappingURL=server.js.map