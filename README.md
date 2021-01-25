# Converter
Simple converter for currencies and units of measurements
===========

Practice #6
===========

![Example](http://i.imgur.com/zi0NXL6.jpg)

The application allows to convert from one
unit of measurement to another. The application includes at least length,
mass, and currency conversion capabilities for now.

The program contains three separate activities

* An activity for buttons leading to other activites
* The lenght and mass conversion activity
* The currency conversion acitivity

`Intent` objects and the `startActivity` method are for switching to another
activities.

UI widgets that you need to use

* `EditText`
* `TextView`
* `Button`
* `Spinner`

Using JSON (`JSONObject`) and XML (`getResources().obtainTypedArray()`) to
store conversion factors and unit/currency names.

Using `SharedPreferences` to load and save the state of the application
(specifically values from all input fields).

Made the currency conversion activity to fetch currency rates from a 3-rd party
provider such as <http://fixer.io>.
