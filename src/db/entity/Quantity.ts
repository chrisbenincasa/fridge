import 'reflect-metadata';

import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';

import { Fridge } from './Fridge';
import { Ingredient } from './Ingredient';

@Entity('quantities')
export class Quantity {
    

    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(type => Fridge, fridge => fridge.quantities)
    fridge: Promise<Fridge>;

    @ManyToOne(type => Ingredient, ingredient => ingredient.quantities, { eager: true })
    ingredient: Ingredient;

    @Column()
    amount: number;

    @Column()
    unit: string;

    add(other: Quantity): Quantity {
        // Normalize unit
        this.amount += other.amount;
        return this;
    }
}