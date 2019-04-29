import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class AddressBookJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AddressBookPane pane = new AddressBookPane();
		Scene scene = new Scene(pane);
		scene.getStylesheets().add("styles.css");
		primaryStage.setTitle("AddressBook");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
	}
}

class AddressBookPane extends GridPane {
	private RandomAccessFile raf;
	// Text fields
	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();
	// Buttons
	private AddButton jbtAdd;
	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	private SortOneButton jbSortOne;
	private SortTwoButton jbSortTwo;
	private IterButton jbIter;
	public EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent arg0) {
			((Command) arg0.getSource()).Execute();
		}
	};

	public AddressBookPane() { // Open or create a random access file
		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(0);
		}
		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(90);
		jtfZip.setPrefWidth(70);
		jbtAdd = new AddButton(this, raf);
		jbtFirst = new FirstButton(this, raf);
		jbtNext = new NextButton(this, raf);
		jbtPrevious = new PreviousButton(this, raf);
		jbtLast = new LastButton(this, raf);
		jbSortOne = new SortOneButton(this, raf);
		jbSortTwo = new SortTwoButton(this, raf);
		jbIter = new IterButton(this, raf);
		Label state = new Label("State");
		Label zp = new Label("Zip");
		Label name = new Label("Name");
		Label street = new Label("Street");
		Label city = new Label("City");
		// Panel p1 for holding labels Name, Street, and City
		GridPane p1 = new GridPane();
		p1.add(name, 0, 0);
		p1.add(street, 0, 1);
		p1.add(city, 0, 2);
		p1.setAlignment(Pos.CENTER_LEFT);
		p1.setVgap(8);
		p1.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);
		// City Row
		GridPane adP = new GridPane();
		adP.add(jtfCity, 0, 0);
		adP.add(state, 1, 0);
		adP.add(jtfState, 2, 0);
		adP.add(zp, 3, 0);
		adP.add(jtfZip, 4, 0);
		adP.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);
		// Panel p4 for holding jtfName, jtfStreet, and p3
		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(adP, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(adP, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(adP, Priority.ALWAYS);
		// Place p1 and p4 into jpAddress
		GridPane jpAddress = new GridPane();
		jpAddress.add(p1, 0, 0);
		jpAddress.add(p4, 1, 0);
		GridPane.setHgrow(p1, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(p1, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);
		// Set the panel with line border
		jpAddress.setStyle("-fx-border-color: grey;" + " -fx-border-width: 1;" + " -fx-border-style: solid outside ;");
		// Add buttons to a panel
		FlowPane jpButton = new FlowPane();
		jpButton.setHgap(5);
		jpButton.getChildren().addAll(jbtAdd, jbtFirst, jbtNext, jbtPrevious, jbtLast, jbSortOne, jbSortTwo, jbIter);
		jpButton.setAlignment(Pos.CENTER);
		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(jpAddress, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(jpAddress, Priority.ALWAYS);
		// Add jpAddress and jpButton to the stage
		this.setVgap(5);
		this.add(jpAddress, 0, 0);
		this.add(jpButton, 0, 1);
		jbtAdd.setOnAction(ae);
		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbSortOne.setOnAction(ae);
		jbSortTwo.setOnAction(ae);
		jbIter.setOnAction(ae);
		jbtFirst.Execute();
	}

	public void actionHandled(ActionEvent e) {
		((Command) e.getSource()).Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}

}

interface Command {
	public void Execute();
}

class CommandButton extends Button implements Command {
	public final static int NAME_SIZE = 32;
	public final static int STREET_SIZE = 32;
	public final static int CITY_SIZE = 20;
	public final static int STATE_SIZE = 10;
	public final static int ZIP_SIZE = 5;
	public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);
	protected AddressBookPane p;
	protected RandomAccessFile raf;

	public CommandButton(AddressBookPane pane, RandomAccessFile r) {
		super();
		p = pane;
		raf = r;
	}

	public void Execute() {
	}

	/** Write a record at the end of the file */
	public void writeAddress() {
		try {
			raf.seek(raf.length());
			FixedLengthStringIO.writeFixedLengthString(p.GetName(), NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetStreet(), STREET_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetCity(), CITY_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetState(), STATE_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetZip(), ZIP_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** Read a record at the specified position */
	public void readAddress(long position) throws IOException {
		raf.seek(position);
		String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
		String street = FixedLengthStringIO.readFixedLengthString(STREET_SIZE, raf);
		String city = FixedLengthStringIO.readFixedLengthString(CITY_SIZE, raf);
		String state = FixedLengthStringIO.readFixedLengthString(STATE_SIZE, raf);
		String zip = FixedLengthStringIO.readFixedLengthString(ZIP_SIZE, raf);
		p.SetName(name);
		p.SetStreet(street);
		p.SetCity(city);
		p.SetState(state);
		p.SetZip(zip);
	}

	public void sort(Comparator<String> comp) throws IOException {
		boolean isSwaped = true;
		for (int i = 0; i < raf.length() - (RECORD_SIZE * 2) && isSwaped; i += RECORD_SIZE * 2) {
			isSwaped = false;
			for (int j = 0; j < raf.length() - (RECORD_SIZE * 2) - i; j += RECORD_SIZE * 2) {
				raf.seek(j);
				String record1 = FixedLengthStringIO.readFixedLengthString(RECORD_SIZE, raf);
				String record2 = FixedLengthStringIO.readFixedLengthString(RECORD_SIZE, raf);
				if (comp.compare(record1, record2) > 0) {
					raf.seek(j);
					FixedLengthStringIO.writeFixedLengthString(record2, RECORD_SIZE, raf);
					FixedLengthStringIO.writeFixedLengthString(record1, RECORD_SIZE, raf);
					isSwaped = true;
				}
			}
		}
	}

	public MyListIterator getIter() {
		return new MyListIterator();
	}

	class NameComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			String s1, s2;
			s1 = o1.substring(0, NAME_SIZE);
			s2 = o2.substring(0, NAME_SIZE);
			return s1.compareTo(s2);
		}
	}

	class ZipComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			int s1, s2;
			s1 = Integer.parseInt((o1.substring(RECORD_SIZE - ZIP_SIZE, RECORD_SIZE)).trim());
			s2 = Integer.parseInt((o2.substring(RECORD_SIZE - ZIP_SIZE, RECORD_SIZE)).trim());
			return s1 - s2;
		}
	}

	class StreetComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			final int START = NAME_SIZE, END = NAME_SIZE + STREET_SIZE;
			String s1 = o1.substring(START, END).trim();
			String s2 = o2.substring(START, END).trim();
			int result = s1.compareTo(s2);
			return result != 0 ? result : 1;
		}
	}

	class MyListIterator implements ListIterator<String> {

		@Override
		public void add(String arg0) {
			try {
				long current = raf.getFilePointer();
				ArrayList<String> list = new ArrayList<String>();
				list.add(arg0);
				while (hasNext())
					list.add(next());
				raf.seek(current);
				for (String string : list)
					FixedLengthStringIO.writeFixedLengthString(string, RECORD_SIZE, raf);
				raf.seek(current + RECORD_SIZE * 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean hasNext() {
			try {
				return raf.getFilePointer() < raf.length() -1 ;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public boolean hasPrevious() {
			try {
				return raf.getFilePointer() >= (RECORD_SIZE * 2);
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public String next() {
			try {
				if (hasNext())
					return FixedLengthStringIO.readFixedLengthString(RECORD_SIZE, raf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int nextIndex() {
			try {
				if (hasNext())
					return (int) raf.getFilePointer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		@Override
		public String previous() {
			if (hasPrevious()) {
				try {
					long pos = raf.getFilePointer() - (RECORD_SIZE * 2);
					raf.seek(pos);
					String next = next();
					raf.seek(pos);
					return next;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public int previousIndex() {
			if (hasPrevious()) {
				try {
					return (int) (raf.getFilePointer() - (RECORD_SIZE * 2));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return -1;
		}

		@Override
		public void remove() {
			try {
				if (raf.length() > 0) {
					long current = raf.getFilePointer();
					ArrayList<String> list = new ArrayList<String>();
					while (hasNext()) 
							list.add(next());
					raf.seek(current);
					for(int i = 1;i<list.size();i++)
						FixedLengthStringIO.writeFixedLengthString(list.get(i), RECORD_SIZE, raf);
					raf.seek(current);
					raf.setLength(raf.length() - RECORD_SIZE * 2);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void set(String arg0) {
			try {
				raf.seek(previousIndex());
				FixedLengthStringIO.writeFixedLengthString(arg0, RECORD_SIZE, raf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class AddButton extends CommandButton {
	public AddButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Add");
	}

	@Override
	public void Execute() {
		writeAddress();
	}
}

class NextButton extends CommandButton {
	public NextButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Next");
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = raf.getFilePointer();
			if (currentPosition < raf.length())
				readAddress(currentPosition);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class PreviousButton extends CommandButton {
	public PreviousButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Previous");
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = raf.getFilePointer();
			if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
				readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
			else
				;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class LastButton extends CommandButton {
	public LastButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Last");
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = raf.length();
			if (lastPosition > 0)
				readAddress(lastPosition - 2 * RECORD_SIZE);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class FirstButton extends CommandButton {
	public FirstButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("First");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() > 0)
				readAddress(0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class SortTwoButton extends CommandButton {
	public SortTwoButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Sort2");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() > 0) {
				sort(new ZipComparator());
				readAddress(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class SortOneButton extends CommandButton {
	public SortOneButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Sort1");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() > 0) {
				sort(new NameComparator());
				readAddress(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class IterButton extends CommandButton {
	private TreeSet<String> treeSet;
	private LinkedHashMap<String, String> hashMap;
	private boolean isClicked = false;

	public IterButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Iter");
	}

	private void goToFirst(ListIterator<String> iter) throws IOException {
		while (iter.hasPrevious())
			iter.previous();
	}

	private void clear(ListIterator<String> iter) throws IOException {
		goToFirst(iter);
		while (iter.hasNext())
			iter.remove();
	}

	private void setMap(ListIterator<String> iter) throws IOException {
		hashMap = new LinkedHashMap<String, String>();
		goToFirst(iter);
		while (iter.hasNext()) {
			String value = iter.next();
			String key = value.substring(0, RECORD_SIZE - ZIP_SIZE);
			hashMap.put(key, value);
		}
		clear(iter);
		for (String key : hashMap.keySet())
			iter.add(hashMap.get(key));
		readAddress(0);
	}

	private void setTree(ListIterator<String> iter) throws IOException {
		treeSet = new TreeSet<String>(new StreetComparator());
		for (String key : hashMap.keySet())
			treeSet.add(hashMap.get(key));
		clear(iter);
		for (String record : treeSet)
			iter.add(record);
		readAddress(0);
	}

	@Override
	public void Execute() {
		try {
			if (!isClicked) {
				isClicked = true;
				setMap(getIter());
			} else {
				setTree(getIter());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}