import React, { Component } from 'react';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tradeData: '',
      result: '',
      loading: false,
      bulkMode: false,
      tradeCount: 0
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
    
    const startTime = Date.now();
    this.setState({ loading: true });
    
    const endpoint = this.state.bulkMode ? 
      'http://localhost:8080/api/validatetrades/bulk' : 
      'http://localhost:8080/api/validatetrades';
    
    fetch(endpoint, {
      mode: 'cors',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: this.state.tradeData
    })
    .then(response => response.text())
    .then(result => {
      const duration = Date.now() - startTime;
      const resultWithTiming = result + '\n\nProcessing time: ' + duration + 'ms';
      this.setState({ result: resultWithTiming, loading: false });
    })
    .catch(error => this.setState({ result: 'Error: ' + error.message, loading: false }));
  }

  loadSample = (type) => {
    this.setState({ tradeData: this.sampleTrades[type], result: '', tradeCount: 1 });
  }

  generateTestData = (count) => {
    this.setState({ loading: true, result: 'Generating ' + count + ' trades...' });
    console.log('Generating test data for count:', count);
    
    fetch('http://localhost:8080/api/generate-test-data?count=' + count, {
      mode: 'cors'
    })
      .then(response => {
        console.log('Response status:', response.status);
        return response.text();
      })
      .then(data => {
        console.log('Data received, length:', data.length);
        console.log('First 200 chars:', data.substring(0, 200));
        
        if (data.includes('<!DOCTYPE html>')) {
          this.setState({ 
            result: 'Error: Received HTML instead of JSON. API call was intercepted.',
            loading: false
          });
        } else {
          this.setState({ 
            tradeData: data, 
            result: 'Successfully generated ' + count + ' trades', 
            loading: false, 
            bulkMode: count > 100,
            tradeCount: count
          });
        }
      })
      .catch(error => {
        console.error('Error:', error);
        this.setState({ result: 'Error generating data: ' + error.message, loading: false });
      });
  }

  downloadJSON = () => {
    if (!this.state.tradeData) {
      alert('No trade data to download');
      return;
    }
    try {
      const blob = new Blob([this.state.tradeData], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'trades-' + this.state.tradeCount + '.json';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (error) {
      alert('Error downloading file: ' + error.message);
    }
  }

  handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const content = e.target.result;
        try {
          const trades = JSON.parse(content);
          this.setState({ 
            tradeData: content, 
            result: '', 
            tradeCount: Array.isArray(trades) ? trades.length : 1,
            bulkMode: Array.isArray(trades) && trades.length > 100
          });
        } catch (error) {
          this.setState({ result: 'Invalid JSON file' });
        }
      };
      reader.readAsText(file);
    }
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
          <h3>Features:</h3>
          <ul>
            <li><strong>Reactive Processing:</strong> RxJava for handling thousands of trades efficiently</li>
            <li><strong>Bulk Validation:</strong> Automatic bulk mode for 100+ trades</li>
            <li><strong>Test Data Generator:</strong> Generate up to 5,000 test trades</li>
            <li><strong>File Upload:</strong> Upload JSON files for validation</li>
            <li><strong>Performance Metrics:</strong> Processing time measurement</li>
          </ul>
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
                Invalid Trade
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.loadSample('option')}
              >
                Vanilla Option
              </button>
            </div>
            
            <div style={styles.buttonGroup}>
              <strong>Generate Test Data:</strong>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(100)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '100'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(1000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '1K'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(5000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '5K'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(10000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '10K'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(50000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '50K'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(100000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '100K'}
              </button>
              <button 
                type="button" 
                style={{...styles.button, ...styles.sampleButton}}
                onClick={() => this.generateTestData(500000)}
                disabled={this.state.loading}
              >
                {this.state.loading ? '...' : '500K'}
              </button>
            </div>
            
            <div style={styles.section}>
              <label style={styles.label}>Upload JSON File:</label>
              <input 
                type="file" 
                accept=".json" 
                onChange={this.handleFileUpload}
                style={{...styles.button, backgroundColor: 'white', border: '1px solid #ddd'}}
              />
            </div>
          </div>

          <div style={styles.section}>
            <label style={styles.label}>
              Trade Data (JSON Array) 
              {this.state.tradeCount > 0 && (
                <span style={{color: '#666', fontWeight: 'normal'}}>
                  - {this.state.tradeCount} trades loaded
                  {this.state.bulkMode && <span style={{color: '#007bff'}}> (Bulk Mode)</span>}
                </span>
              )}
            </label>
            <textarea
              style={styles.textarea}
              value={this.state.tradeData}
              onChange={(e) => {
                const value = e.target.value;
                try {
                  const trades = JSON.parse(value || '[]');
                  this.setState({ 
                    tradeData: value, 
                    tradeCount: Array.isArray(trades) ? trades.length : 0,
                    bulkMode: Array.isArray(trades) && trades.length > 100
                  });
                } catch (error) {
                  this.setState({ tradeData: value });
                }
              }}
              placeholder="Enter JSON array of trades, upload a file, or generate test data"
            />
          </div>

          <div style={styles.buttonGroup}>
            <button 
              type="submit" 
              style={{...styles.button, ...styles.primaryButton}}
              disabled={this.state.loading}
            >
              {this.state.loading ? 'Validating...' : 
               (this.state.bulkMode ? 'Validate Trades (Reactive)' : 'Validate Trades')}
            </button>
            {this.state.tradeData && (
              <button 
                type="button" 
                style={{...styles.button, backgroundColor: '#28a745', color: 'white'}}
                onClick={this.downloadJSON}
              >
                Download JSON
              </button>
            )}
          </div>
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
