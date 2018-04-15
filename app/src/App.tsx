import React, { Component } from 'react';
import { ActivityIndicator, FlatList, StyleSheet, Text, View } from 'react-native';

import Config from './Config';

interface Fridge {
  id: number;
  name: string;
}

interface Props {};
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
            renderItem={({item}) => <Text style={styles.instructions}>{item.id}, {item.name}</Text>}
            keyExtractor={(item, index) => index.toString()}
          />
        </View>
      );
    }
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
    marginVertical: 50,
    marginLeft: 10
  },
  instructions: {
    textAlign: 'left',
    color: '#333333',
    marginBottom: 5,
  },
});
