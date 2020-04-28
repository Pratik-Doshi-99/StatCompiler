# StatCompiler
This repository contains necessary code for a GUI application that compiles basic statistics for equities listed on NSE, India. The application runs on a self developed API (Version 2.0 of repository named "Indian-Stock-Market-Data").

The application calculates the mean and standard deviation of the daily returns of Indian equities listed on NSE. It requires a list of the NSE ticker symbols for which the above calculation is to be made, the start and the end date(optional) of the period in which the computation has to be made and a target folder where the compiled file has to be saved.

Output: A compiled (.csv) file that contains the mean and standard deviation of returns of all scrips in the list.

Inputs:
start date
end date (by default it is current date)
location of csv file
location of target directory
