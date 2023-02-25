import React, {useState, useEffect} from 'react';
import axios from 'axios';
import plFlag from './images/pl-flag.png';
import ukFlag from './images/uk-flag.png';

const PLN = 'PLN';
const GBP = 'GBP';

function ConverterComponent() {

    const [value1, setValue1] = useState('');
    const [value2, setValue2] = useState('');

    const [mid, setMid] = useState(0);

    useEffect(() => {
        axios.get("http://localhost:8080/api/mid-value")
            .then(response => setMid(response.data))
            .catch(error => {
                console.error(error);
                alert('There was an error processing your request. Please try again later.');
            });
    }, []);

    const handleValueChange = (value, name) => {
        const inputValue = Number(value);
        const inputElement = document.getElementsByName(name)[0];
        if (isNaN(inputValue) || inputValue < 0) {
            inputElement.classList.add('error');
            return;
        }
        if (name === PLN) {
            inputElement.classList.remove('error');
            setValue1(value);
            axios.post('http://localhost:8080/api/convert-currency', {value, name})
                .then(response => setValue2(response.data))
                .catch(error => {
                    console.log(error);
                    alert('There was an error processing your request. Please try again later.');
                });
        } else if (name === GBP) {
            inputElement.classList.remove('error');
            setValue2(value);
            axios.post('http://localhost:8080/api/convert-currency', {value, name})
                .then(response => setValue1(response.data))
                .catch(error => {
                    console.log(error);
                    alert('There was an error processing your request. Please try again later.');
                });
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="text-start">

                You send
                <div className="input-group mb-3">
                    <span className="input-group-text"><img src={ukFlag} alt="uk-flag"/></span>
                    <input type="text" className="form-control"
                           name={PLN} value={value1} onChange={e => handleValueChange(e.target.value, e.target.name)}
                    />
                    <span className="input-group-text">{GBP}</span>
                </div>

                They receive
                <div className="input-group">
                    <span className="input-group-text"><img src={plFlag} alt="pl-flag"/></span>
                    <input type="text" className="form-control"
                           name={GBP} value={value2} onChange={e => handleValueChange(e.target.value, e.target.name)}
                    />
                    <span className="input-group-text">{PLN}</span>
                </div>

                <br/>
                1 GBP = <b>{mid} PLN</b>

            </div>
        </div>
    );
}

export default ConverterComponent;
