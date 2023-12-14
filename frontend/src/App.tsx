import {useState} from "react";
import {MakeADT, makeMatchI} from "ts-adt/MakeADT";
import * as O from "fp-ts/Option";
import './App.css';
import {pipe} from "fp-ts/function";

type ApiResponseData = MakeADT<
    "status",
    {
        OK: { validIban: string, bankName?: string };
        BAD_REQUEST: { invalidIban: string, errorMessage: string };
    }
>;

const matchI = makeMatchI("status");

function IbanValidator() {

    const [response, setResponse] = useState<O.Option<ApiResponseData>>(O.none)
    const handleSubmit = (e: any) => {
        // Prevent the browser from reloading the page
        e.preventDefault();

        // Read the form data
        const form = e.target;
        const formData = new FormData(form);
        const formDataObject = Object.fromEntries(formData.entries());
        // Format the plain form data as JSON
        const formDataJsonString = JSON.stringify(formDataObject);

        // Pass formDataJsonString as a fetch body directly:
        fetch('/api/validateIban', {
            method: form.method,
            body: formDataJsonString,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }).then(
            response =>
                response.json()
        ).then(
            data => {
                setResponse(O.some(data))
            }
        ).catch(
            error => console.error(error)
        );
    };

    return (<form method="post" onSubmit={handleSubmit}>
        <label>IBAN to validate<input name="ibanToValidate" type="text" placeholder="IBAN"/></label>
        <button type="submit">Validate</button>
        <IbanValidationResult response={response}/>
    </form>)
}

function IbanValidationResult({response}: { response: O.Option<ApiResponseData> }) {
    return pipe(
        response,
        O.fold(
            () => null,
            (data: ApiResponseData) =>
                matchI(data)({
                    OK: ({validIban, bankName}) => {
                        return <p>
                            The IBAN {validIban} is valid {bankName && `and belongs to ${bankName}`}
                        </p>
                    },
                    BAD_REQUEST: ({invalidIban, errorMessage}) => {
                        return <p>
                            The IBAN {invalidIban} is invalid: {errorMessage}
                        </p>
                    },
                    // Add a default function to handle unexpected variants
                    default: () => {
                        return <p>
                            Received unexpected server response
                        </p>
                    }
                })
        )
    )
}

function App() {
    return (
        <div className="App">
            <IbanValidator/>
        </div>
    );
}

export default App;
