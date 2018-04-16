import React, { Component } from 'react';
import { ActivityIndicator, StyleSheet, FlatList, ListRenderItem, Text, View } from 'react-native';

import Config from '../Config';
import Fridge from '../model/Fridge';
import Quantity from '../model/Quantity';

interface Props {
    fridgeId: number;
    navigator: Navigator
};

interface State {
    loading: boolean;
    data?: Fridge
};

export default class FridgeDetail extends Component<Props> {
    public state: State = { loading: true };

    constructor(props: Props) {
        super(props);
    }

    componentDidMount() {
        return fetch(`${Config.server.host}/fridges/${this.props.fridgeId}`).then(async response => {
            let jsonData = await response.json();

            console.log(jsonData.data);

            this.setState({
                loading: false,
                data: jsonData.data
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
            )
        } else {
            const renderItem: ListRenderItem<Quantity> = ({ item }) => (
                <Text>{item.ingredient.name}, {item.amount} {item.unit}</Text>
            );

            return (
                <View style={styles.container}>
                    <FlatList
                        data={this.state.data.quantities}
                        renderItem={renderItem}
                        keyExtractor={(item, index) => index.toString()}
                    />
                </View>
            )
        }
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'flex-start',
        alignItems: 'flex-start',
        backgroundColor: '#F5FCFF',
    }
});