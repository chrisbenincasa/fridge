import { Navigation } from 'react-native-navigation';
import { Provider } from 'react-redux';

import FridgeList from './src/screens/FridgeList';
import FridgeDetail from './src/screens/FridgeDetail';
import configureStore from './src/store/ConfigureStore';

const store = configureStore();

// registerScreens(); // this is where you register all of your app's screens
Navigation.registerComponent('fridge.FridgeList', () => FridgeList, store, Provider);
Navigation.registerComponent('fridge.FridgeDetail', () => FridgeDetail, store, Provider);

// start the app
Navigation.startSingleScreenApp({
  screen: {
      screen: 'fridge.FridgeList',
      title: 'Fridges'
  }
});