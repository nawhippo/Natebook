import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import About from './pages/About';
class App extends Component {
  state = {
    data: null,
    isLoading: true,
    error: null,
  };

  async componentDidMount() {
    try {
      const response = await fetch('/api/home');
      if (!response.ok) throw new Error('Network response was not ok');
      const data = await response.json();
      this.setState({ data, isLoading: false });
    } catch (error) {
      this.setState({ error: error.message, isLoading: false });
    }
  }

  render() {
    return (
      <Router>
        <div className="App">
          <header className="App-header">
            <Switch>
              <Route exact path="/" render={() => (
                <div>
                  <div className="App-intro">
                    Hey! {this.state.data && this.state.data.message}
                  </div>
                </div>
              )} />
              <Route path="/about" component={About} />
            </Switch>
          </header>
        </div>
      </Router>
    );
  }
}


export default App;