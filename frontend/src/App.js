import React, { Component } from 'react';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tradeData: '',
      result: '',
      loading: false
    };
  }

  sampleTrades = {
    valid: `[
  {
    "customer": "PLUTO1",
    "ccyPair": "EURUSD",
    "type": "Spot",
    "direction": "BUY",
    "tradeDate": "2016-08-11",
    "amount1": 1000000.00,
    "amount2": 1120000.00,
    "rate": 1.12,
    "valueDate": "2016-08-15",
    "legalEntity": "CS Zurich",
    "trader": "Johann Baumfiddler"
  }
]`,
    invalid: `[
  {
    "customer": "PLUTO1",
    "ccyPair": "EURUSD",
    "type": "Spot",
    "direction": "BUY",
    "tradeDate": "2016-08-15",
    "amount1": 1000000.00,
    "amount2": 1120000.00,
    "rate": 1.12,
    "valueDate": "2016-08-11",
    "legalEntity": "CS Zurich",
    "trader": "Johann Baumfiddler"
  }
]`,
    option: `[
  {
    "customer": "PLUTO2",
    "ccyPair": "EURUSD",
    "type": "VanillaOption",
    "style": "EUROPEAN",
    "direction": "BUY",
    "strategy": "CALL",
    "tradeDate": "2016-08-11",
    "amount1": 1000000.00,
    "amount2": 1120000.00,
    "rate": 1.12,
    "deliveryDate": "2016-08-22",
    "expiryDate": "2016-08-19",
    "payCcy": "USD",
    "premium": 0.20,
    "premiumCcy": "USD",
    "premiumType": "%USD",
    "premiumDate": "2016-08-12",
    "legalEntity": "CS Zurich",
    "trader": "Johann Baumfiddler"
  }
]`
  };

  handleSubmit = (e) => {
    e.preventDefault();
    if (!this.state.tradeData.trim()) {
      this.setState({ result: 'Please enter trade data' });
      return;
    }
    this.setState({ loading: true });
    fetch('/api/validatetrades', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: this.state.tradeData
    })
    .then(response => response.text())
    .then(result => this.setState({ result, loading: false }))
    .catch(error => this.setState({ result: 'Error: ' + error.message, loading: false }));
  }

  loadSample = (type) => {
    this.setState({ tradeData: this.sampleTrades[type], result: '' });
  }

  render() {
    const styles = {
      container: {
        maxWidth: '1200px',
        margin: '0 auto',
        padding: '20px',
        fontFamily: 'Arial, sans-serif'
      },
      header: {
        textAlign: 'center',
        color: '#333',
        marginBottom: '30px'
      },
      section: {
        marginBottom: '20px'
      },
      label: {
        display: 'block',
        marginBottom: '10px',
        fontWeight: 'bold',
        color: '#555'
      },
      textarea: {
        width: '100%',
        minHeight: '200px',
        padding: '10px',
        border: '1px solid #ddd',
        borderRadius: '4px',
        fontSize: '12px',
        fontFamily: 'monospace'
      },
      buttonGroup: {
        marginBottom: '15px'
      },
      button: {
        padding: '10px 15px',
        margin: '5px',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        fontSize: '14px'
      },
      primaryButton: {
        backgroundColor: '#007bff',
        color: 'white'
      },
      sampleButton: {
        backgroundColor: '#6c757d',
        color: 'white'
      },
      result: {
        marginTop: '20px',
        padding: '15px',
        border: '1px solid #ddd',
        borderRadius: '4px',
        backgroundColor: '#f8f9fa'
      },
      pre: {
        whiteSpace: 'pre-wrap',
        margin: 0,
        fontSize: '12px'
      },
      info: {
        backgroundColor: '#e7f3ff',
        padding: '15px',
        borderRadius: '4px',
        marginBottom: '20px'
      }
    };

    return (
      <div style={styles.container}>
        <h1 style={styles.header}>Financial Trade Validator</h1>
        
        <div style={styles.info}>
          <h3>Validation Rules:</h3>
          <ul>
            <li>Value date cannot be before trade date</li>
            <li>Trade dates cannot fall on weekends</li>
            <li>Currency codes must be valid ISO 4217</li>
            <li>Customer must exist in system</li>
            <li>Option style validation (American/European)</li>
            <li>Exercise and expiry date consistency</li>
          </ul>
        </div>

        <form onSubmit={this.handleSubmit}>
          <div style={styles.section}>
            <label style={styles.label}>Sample Data:</label>
            <div style={styles.buttonGroup}>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.loadSample('valid')}
              >
                Valid Spot Trade
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.loadSample('invalid')}
              >
                Invalid Trade (Value Date Error)
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.loadSample('option')}
              >
                Vanilla Option
              </button>
            </div>
          </div>

          <div style={styles.section}>
            <label style={styles.label}>Trade Data (JSON Array):</label>
            <textarea
              style={styles.textarea}
              value={this.state.tradeData}
              onChange={(e) => this.setState({ tradeData: e.target.value })}
              placeholder="Enter JSON array of trades or use sample data above"
            />
          </div>

          <button 
            type="submit" 
            style={{...styles.button, ...styles.primaryButton}}
            disabled={this.state.loading}
          >
            {this.state.loading ? 'Validating...' : 'Validate Trades'}
          </button>
        </form>

        {this.state.result && (
          <div style={styles.result}>
            <h3>Validation Result:</h3>
            <pre style={styles.pre}>{this.state.result}</pre>
          </div>
        )}
      </div>
    );
  }
}

export default App;
