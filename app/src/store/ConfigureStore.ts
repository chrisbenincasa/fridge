import { createStore, applyMiddleware } from 'redux';
import rootReducer from '../reducers/RootReducer';
import thunk from 'redux-thunk';
import Fridge from '../model/Fridge';

const middleware = [thunk]

export interface AppState {
    fridge?: Fridge
};

export const initialState: AppState = {};

export default function configureStore(initialState: AppState) {
    createStore(
        rootReducer,
        initialState,
        applyMiddleware(...middleware)
    )
}