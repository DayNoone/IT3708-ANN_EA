package project5;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Alexander on 24/04/2016.
 */
public class XLSXReader {

    public XLSXReader() {
    }

    public int[][] read(String s, int numCities){

        File myFile = new File(s);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int[][] tempList = new int[numCities][numCities];

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = null;

        try {
            myWorkBook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        for (int row = 0; row < numCities; row++) {
            for (int col = 0; col < mySheet.getRow(row+1).getLastCellNum()-1; col++) {
                Cell temp = mySheet.getRow(row+1).getCell(col+1);
                if(temp.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    tempList[row][col] = (int) temp.getNumericCellValue();
                    tempList[col][row] = (int) temp.getNumericCellValue();

                }
            }
        }
        return tempList;
    }


}
