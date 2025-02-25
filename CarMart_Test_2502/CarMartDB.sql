CREATE TABLE Company (
    companyID SERIAL PRIMARY KEY,
    companyName VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Car (
    carID SERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    seater INT NOT NULL,
    fuelType VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    companyID INT REFERENCES Company(companyID) ON DELETE CASCADE
);

CREATE TABLE Sale (
    saleID SERIAL PRIMARY KEY,
    carID INT REFERENCES Car(carID) ON DELETE CASCADE,
    sold BOOLEAN DEFAULT FALSE
);
INSERT INTO Company (companyName) VALUES ('Toyota');
INSERT INTO Company (companyName) VALUES ('Honda');
INSERT INTO Company (companyName) VALUES ('Ford');
INSERT INTO Company (companyName) VALUES ('Tesla');

select * from Company;
select * from Car;
select * from Sale;