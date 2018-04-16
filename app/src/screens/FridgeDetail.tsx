import React, { Component } from 'react';
import { ActivityIndicator, View } from 'react-native';

interface Props {};

export default class FridgeDetail extends Component<Props> {
    render() {
        return (
            <View>
                <ActivityIndicator />
            </View>
        )
    }
}