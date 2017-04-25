# Lab 7 Bugs

Zachary Griggs
Kay Thao

| Bug Num. | Original Line Number | Description                                                                                              | Fix             |
|----------|----------------------|----------------------------------------------------------------------------------------------------------|-----------------|
| 1        | Line 150             | Checks is quote is not equal and throws exception if it's not, where it should throw exception if it is. | Change != to == |
| 2        | Line 184             | Throws NullPointerException by mistake instead of InvalidAnalysisStateException                                                                    |Change NullPointerException to InvalidAnalysisState    |
| 3        | Line 204             | Calculation of percent has an extra factor of 10, any percent returned is ten times expected.                                                                                                         | Change multiplier from 100000 to 10000   |
| 4        | Line 220             | It calls current price - current price, which always returns zero.                                                                                                         | Change to subtract from previous quote instead of current.   |
| 5        | Line 219-221             | Does not check whether there have been stocks loaded before trying to calculate                                                                                                         | Added null checks for previous quote (subsumes checking for current quote)  |
| 5        | Line 119             | Plays happy music whenever percent is over zero; expected is greater than or equal to 1%.                                                                                                        | Changes > 0 to >= 1  |
