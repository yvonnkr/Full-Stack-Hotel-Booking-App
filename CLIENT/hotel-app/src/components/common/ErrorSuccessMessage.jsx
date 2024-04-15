const ErrorSuccessMessage = ({ errorMessage, successMessage }) => {
  return (
    <>
      {errorMessage && <p className="alert alert-danger">{errorMessage}</p>}

      {successMessage && (
        <p className="alert alert-success">{successMessage}</p>
      )}
    </>
  );
};

export default ErrorSuccessMessage;
