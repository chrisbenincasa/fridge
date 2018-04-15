import { Navigation } from 'react-native-navigation';

import App from './src/screens/App';

// registerScreens(); // this is where you register all of your app's screens
Navigation.registerComponent('Main', () => App);

// start the app
Navigation.startSingleScreenApp({
  screen: {
      screen: 'Main',
      title: 'Main'
  }
});