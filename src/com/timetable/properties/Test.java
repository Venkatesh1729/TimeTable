package com.timetable.properties;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test
{

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		Random random = new Random();
		Map<String, String[][]> mapTM = new HashMap<String, String[][]>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("T");
		list1.add("E");
		list1.add("M");
		list1.add("P");
		list1.add("C");
		list1.add("SS");

		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("T");
		list2.add("E");
		list2.add("M");
		list2.add("P");
		list2.add("C");
		list2.add("SS");

		ArrayList<String> list3 = new ArrayList<String>();
		list3.add("T");
		list3.add("E");
		list3.add("M");
		list3.add("P");
		list3.add("C");
		list3.add("SS");
		
		ArrayList<String> list4 = new ArrayList<String>();
		list4.add("T");
		list4.add("Ex");
		list4.add("M");
		list4.add("Px");
		list4.add("C");
		list4.add("ScS");

		ArrayList<String> list5 = new ArrayList<String>();
		list5.add("Tf");
		list5.add("E");
		list5.add("M");
		list5.add("Pd");
		list5.add("C");
		list5.add("SS");

		ArrayList<String> list6 = new ArrayList<String>();
		list6.add("Tf");
		list6.add("E");
		list6.add("Mf");
		list6.add("P");
		list6.add("Cd");
		list6.add("SS");

		map.put("C1", list1);
		map.put("C2", list2);
		map.put("C3", list3);
		map.put("C4", list4);
		map.put("C5", list5);
		map.put("C6", list6);
		
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.shuffle(keys);

		for (Object string : keys) {
			int row = 6;
			int column = 8;
			int i = 0;
			int j = 0;
			boolean reset =false;
			String[][] stringArray = new String[row][column];
			List<String> temp = map.get(string);
			Collections.shuffle(temp);
			for (int iterator = 0; iterator < column; iterator++) {
				
				if(iterator >= temp.size()){
					int randomInteger = random.nextInt(temp.size());
					stringArray[i][j] = temp.get(randomInteger);
				}else{
					stringArray[i][j] = temp.get(iterator);
				}
				
				for(Object string01 : keys){
					if(mapTM.get(string01) != null && !string01.equals(string)){
						String[][] temp01 = mapTM.get(string01);
						if(temp01[i][j] != null && temp01[i][j].equals(stringArray[i][j])){
							j = 0;
							Collections.shuffle(temp);
							iterator = -1;
							reset = true;
						}
					}
				}
				if(reset){
					reset = false;
					continue;
				}
				
				if (j < column && j != column - 1) {
					j++;
				} else if (j == column - 1 && i != row - 1) {
					i++;
					j = 0;
					Collections.shuffle(temp);
					iterator = -1;
				} else if (j == column - 1 && i == row - 1) {
					break;
				}
			}
			mapTM.put((String) string, stringArray);
		}
		
		for (Map.Entry<String, String[][]> entry : mapTM.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(Arrays.deepToString(entry.getValue()));
		}

	}

}
