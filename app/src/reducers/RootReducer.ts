import { combineReducers, Reducer, AnyAction } from 'redux';
import { AppState, initialState } from '../store/ConfigureStore';

const noopReducer: Reducer<AppState> = function(state: AppState = initialState, action: AnyAction): AppState {
    return {};
}

const rootReducer = combineReducers({
    noopReducer
});

export default rootReducer;