import 'reflect-metadata';

import { Column, Entity, JoinTable, ManyToMany, PrimaryGeneratedColumn } from 'typeorm';

import { Ingredient } from './Ingredient';
import { Quantity } from './Quantity';

@Entity('fridges')
export class Fridge {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @ManyToMany(type => Quantity, q => q.fridge, { eager: true })
    @JoinTable({ name: 'fridge_quantities' })
    quantities: Quantity[];
}

export class AvailableIngredient {
    quantity: Quantity;
    ingredient: Ingredient;

    constructor(q: Quantity, i: Ingredient) {
        this.quantity = q;
        this.ingredient = i;
    }
}