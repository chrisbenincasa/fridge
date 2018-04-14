import 'reflect-metadata';
import {Entity, Column, PrimaryGeneratedColumn, OneToMany} from 'typeorm';
import { Quantity } from './Quantity';

@Entity('ingredients')
export class Ingredient {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @OneToMany(type => Quantity, q => q.ingredient)
    quantities: Quantity[]
}