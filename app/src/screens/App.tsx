import React, { Component } from 'react';
import { Navigator } from 'react-native-navigation';
import { ActivityIndicator, FlatList, StyleSheet, Text, View } from 'react-native';

import Config from '../Config';

interface Fridge {
  id: number;
  name: string;
}

interface Props {
  navigator: Navigator
};
interface State {
  loading: boolean;
  dataSource: Fridge[]
};
export default class App extends Component<Props, State> {
  public state: State = { loading: true, dataSource: [] }

  constructor(props: Props) {
    super(props);
  }

  componentDidMount() {
    return fetch(`${Config.server.host}/fridges`).then(async response => {
      let respJson = await response.json();

      this.setState({ 
        loading: false,
        dataSource: respJson.data
      });
    }).catch(e => {
      console.error(e);
    });
  }

  render() {
    if (this.state.loading) {
      return (
        <View style={styles.container}>
          <ActivityIndicator />
        </View>
      );
    } else {
      return (
        <View style={styles.container}>
          <FlatList 
            style={styles.welcome}
            data={this.state.dataSource} 
            renderItem={({item}) => <Text onPress={() => this.onFridgeClicked(item)} style={styles.instructions}>{item.id}, {item.name}</Text>}
            keyExtractor={(item, index) => index.toString()}
          />
        </View>
      );
    }
  }

  onFridgeClicked(item: Fridge) {
    this.props.navigator.push({
      screen: 'Main',
      title: item.name
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    marginVertical: 0,
    marginLeft: 10
  },
  instructions: {
    textAlign: 'left',
    color: '#333333',
    marginBottom: 5,
  },
});