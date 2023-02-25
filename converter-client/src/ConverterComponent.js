import React, {useState, useEffect} from 'react';
import axios from 'axios';

function ConverterComponent() {

    const [value1, setValue1] = useState('');
    const [value2, setValue2] = useState('');

    let plFlag = require('./images/pl-flag.png');
    let ukFlag = require('./images/uk-flag.png');

    const [mid, setMid] = useState(0);

    const [value1ChangedByUser, setValue1ChangedByUser] = useState(false);
    const [value2ChangedByUser, setValue2ChangedByUser] = useState(false);

    useEffect(() => {
        axios.get("http://localhost:8080/api/mid-value")
            .then(response => setMid(response.data))
            .catch(error => console.error(error));
    }, []);

    useEffect(() => {
        if (value1ChangedByUser) {
            axios.post('http://localhost:8080/api/process-value', {value: value1, name: 'PLN'})
                .then(response => setValue2(response.data))
                .catch(error => console.log(error));
            setValue1ChangedByUser(false);
        }
    }, [value1, value1ChangedByUser]);

    const handleValue1Change = (e) => {
        const inputValue = e.target.value;
        if (isNaN(inputValue) || inputValue < 0) {
            e.target.classList.add('error');
        } else {
            setValue1(e.target.value);
            setValue1ChangedByUser(true);
            e.target.classList.remove('error');
        }
    }

    useEffect(() => {
        if (value2ChangedByUser) {
            axios.post('http://localhost:8080/api/process-value', {value: value2, name: 'GBP'})
                .then(response => setValue1(response.data))
                .catch(error => console.log(error));
            setValue2ChangedByUser(false);
        }
    }, [value2, value2ChangedByUser]);

    const handleValue2Change = (e) => {
        const inputValue = e.target.value;
        if (isNaN(inputValue) || inputValue < 0) {
            e.target.classList.add('error');
        } else {
            setValue2(e.target.value);
            setValue2ChangedByUser(true);
            e.target.classList.remove('error');
        }
    }

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="text-start">

                You send
                <div className="input-group mb-3">
                    <span className="input-group-text"><img src={ukFlag} alt="pl-flag"/></span>
                    <input type="text" className="form-control"
                           name="PLN" value={value1} onChange={handleValue1Change}
                    />
                    <span className="input-group-text">GBP</span>
                </div>

                They receive
                <div className="input-group">
                    <span className="input-group-text"><img src={plFlag} alt="pl-flag"/></span>
                    <input type="text" className="form-control"
                           name="GBP" value={value2} onChange={handleValue2Change}/>
                    <span className="input-group-text">PLN</span>
                </div>

                <br/>

                1 GBP = <b>{mid} PLN</b>

            </div>
        </div>
    );
}

export default ConverterComponent;
