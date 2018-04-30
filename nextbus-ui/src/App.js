
import React, { Component } from 'react';
import { DropdownButton } from 'react-bootstrap';
import { MenuItem } from 'react-bootstrap';
import './App.css';

class App extends Component {

    constructor() {
        super();
        this.state = {
            routes: [],
            directions: [],
            stops: [],
            selectedRoute: '',
            selectedDirection: '',
            selectedStop: '',
            message: ''
        };
    }

    componentDidMount() {
        var err,
            url = '/api/nextbus/v1/list';

        fetch(url).then(response => {
            return response.json();
        }).then(json => {
            this.setState({
                routes: json,
                directions: [],
                stops: [],
                selectedRoute: '',
                selectedDirection: '',
                selectedStop: '',
                message: ''
            });
        }).catch(err);
    }

    onRouteSelected(event, eventKey) {

        var route = this.state.routes[event],
            url = '/api/nextbus/v1/list/directions',
            err = '';

        console.log('Selected route: ' + route.Description);

        fetch(url, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(route)
        }).then(response => {
            return response.json();
        }).then(json => {
            var routes = this.state.routes;
            this.setState({
                routes: routes,
                directions: json,
                stops: [],
                selectedRoute: route,
                selectedDirection: '',
                selectedStop: '',
                message: ''
            });
            console.log(json);
        }).catch(err);
    }

    onDirectionSelected(event, eventKey) {
        var routes = this.state.routes,
            route = this.state.selectedRoute,
            directions = this.state.directions,
            direction = this.state.directions[event],
            url = '/api/nextbus/v1/list/stops',
            request = {
                route: route,
                direction: direction
            },
            err = '';

        console.log('Selected direction: ' + direction);

        fetch(url, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        }).then(response => {
            return response.json();
        }).then(json => {
            this.setState({
                routes: routes,
                directions: directions,
                stops: json,
                selectedRoute: route,
                selectedDirection: direction,
                selectedStop: '',
                message: ''
            });
            console.log(json);
        }).catch(err);
    }

    onStopSelected(event, eventKey) {
        var routes = this.state.routes,
            route = this.state.selectedRoute,
            directions = this.state.directions,
            direction = this.state.selectedDirection,
            stops = this.state.stops,
            stop = this.state.stops[event],
            url = '/api/nextbus/v1/nextbus/' + route.Description + '/' + stop.Text + '/' + direction,
            err = '';

        console.log('Selected stop: ' + stop.Text);

        fetch(url).then(response => {
            return response.json();
        }).then(json => {
            console.log(json);
            this.setState({
                routes: routes,
                directions: directions,
                stops: stops,
                selectedRoute: route,
                selectedDirection: direction,
                selectedStop: stop,
                message: json.message
            });
        }).catch(err);
    }

    render() {
        var i = 1;
        return (
            <div>
            <DropdownButton
                title='Routes'
                key={i}
                id={`dropdown-basic-${i}`}
                onSelect={ this.onRouteSelected.bind(this) }
                >
                {this.state.routes.map(
                    (route, index) =>
                        <MenuItem key={ index } eventKey={ index }>{ route.Description }</MenuItem>
                )}
            </DropdownButton>
            <DropdownButton
              title='Directions'
              key={i+1}
              id={`dropdown-basic-${i+1}`}
              onSelect={ this.onDirectionSelected.bind(this) }
              >
              {this.state.directions.map(
                  (direction, index) =>
                      <MenuItem key={ index } eventKey={ index }>{ direction }</MenuItem>
              )}
            </DropdownButton>
            <DropdownButton
              title='Stops'
              key={i+2}
              id={`dropdown-basic-${i+2}`}
              onSelect={ this.onStopSelected.bind(this) }
              >
              {this.state.stops.map(
                  (stop, index) =>
                      <MenuItem key={ index } eventKey={ index }>{ stop.Text }</MenuItem>
              )}
            </DropdownButton>
            <div>Selected Route: {this.state.selectedRoute.Description}</div>
            <div>Selected Direction: {this.state.selectedDirection}</div>
            <div>Selected Stop: {this.state.selectedStop.Text}</div>
            <div>Time to next departure: {this.state.message}</div>
            </div>
        );
    }
}

export default App;
