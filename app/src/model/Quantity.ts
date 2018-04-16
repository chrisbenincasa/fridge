import Ingredient from "./Ingredient";

export default interface Quantity {
    ingredient: Ingredient;
    amount: number;
    unit: string;
}