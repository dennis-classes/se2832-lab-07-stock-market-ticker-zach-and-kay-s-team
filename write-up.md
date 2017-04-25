#Zachary Griggs
#Kay Thao

| Bug Num. | Original Line Number | Description                                                                                              | Fix             |
|----------|----------------------|----------------------------------------------------------------------------------------------------------|-----------------|
| 1        | Line 150             | Checks is quote is not equal and throws exception if it's not, where it should throw exception if it is. | Change != to == |
| 2        | Line 184             | Throws NullPointerException by mistake instead of InvalidAnalysisStateException                                                                    |Change NullPointerException to InvalidAnalysisState    |
| 3        | Line 204             | Calculation of percent has an extra factor of 10, any percent returned is ten times expected.                                                                                                         | Change multiplier from 100000 to 10000   |
