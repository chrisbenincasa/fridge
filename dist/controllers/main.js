"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const controller_1 = require("./controller");
class MainController extends controller_1.Controller {
    constructor(router) {
        super(router);
    }
    setupRoutes() {
        this.router.get('/*', async (ctx) => {
            ctx.body = { "response": "Hello, World!" };
        });
    }
}
exports.MainController = MainController;
class Controllers {
    static getControllers(router) {
        return [
            new MainController(router)
        ];
    }
}
exports.Controllers = Controllers;
//# sourceMappingURL=main.js.map