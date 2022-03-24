package modal.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modal.entities.DirectoryList;

public class DirectoryService {

	public List<DirectoryList> findAll() {

		List<DirectoryList> list = new ArrayList<>();

		File path = new File("DiskLocalC.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			while (line != null) {
				list.add(new DirectoryList(line));
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

		return list;

	}

	public List<DirectoryList> findAll2() {

		List<DirectoryList> list = new ArrayList<>();

		File path = new File("UserProfile.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			while (line != null) {
				list.add(new DirectoryList(line));
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

		return list;

	}

}
