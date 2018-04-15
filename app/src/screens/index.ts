import { Navigation } from 'react-native-navigation';
import App from '../screens/App';

// register all screens of the app (including internal ones)
export function registerScreens() {
    Navigation.registerScreen('Main', () => App);
}