import Quantity from "./Quantity";

export default interface Fridge {
    id: number;
    name: string;
    quantities: Quantity[]
};