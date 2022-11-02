/*
* This is a function for the navara assessment
*/
const VALIDATE_BUTTON = document.getElementById('validate_button');
const TYPE_PERSON_BUTTON = document.getElementById('type_person');
const TYPE_COMPANY_BUTTON = document.getElementById('type_company');
const MAIL_FIELD = document.getElementById('email');

const validPersonFields = () => {
    const firstNameField = document.getElementById('first_name');
    const lastNameField = document.getElementById('last_name');
    const validPerson = firstNameField.value !== '' &&
        lastNameField.value !== '' &&
        validateEmail(MAIL_FIELD.value);
    console.log('Valid person: ' +  validPerson);
    return validPerson;
};

const validCompanyFields = () => {
    //Valid phone number example: 123-456-7890
    const phonenumber = /^\(?(\d{3})\)?[-. ]?(\d{3})[-. ]?(\d{4})$/;
    const companyNameField = document.getElementById('company_name');
    const phoneField = document.getElementById('phone');

    const valid = companyNameField.value !== '' &&
        phoneField.value !== '' &&
        phonenumber.test(String(phoneField.value).toLowerCase());
    console.log('Valid company: ' + valid);
    return valid;
};

const validateEmail = email => {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
};

const validate = () => {
    let valid;
    if (TYPE_PERSON_BUTTON.checked && TYPE_COMPANY_BUTTON.checked) {
        valid = validPersonFields() && validCompanyFields();
    } else if (TYPE_COMPANY_BUTTON.checked) {
        valid = validCompanyFields();
    } else if (TYPE_PERSON_BUTTON.checked) {
        valid = validPersonFields();
    } else {
        valid = false;
    }
    console.log('All valid: ' + valid);
    return valid;
};

const validateAndSetButtonColor = () => {
    const valid = validate();
    VALIDATE_BUTTON.style.backgroundColor = valid ? 'green' : 'red';
};

MAIL_FIELD.addEventListener('change',
    () =>  MAIL_FIELD.style.backgroundColor = validateEmail(MAIL_FIELD.value) ? 'white' : 'red');
VALIDATE_BUTTON.addEventListener('click', validateAndSetButtonColor);
