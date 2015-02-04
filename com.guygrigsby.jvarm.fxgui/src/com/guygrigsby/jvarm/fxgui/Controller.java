package com.guygrigsby.jvarm.fxgui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import com.guygrigsby.jvarm.core.ArmProgram;
import com.guygrigsby.jvarm.core.ArmSourceCompiler;
import com.guygrigsby.jvarm.core.test.ArmSourceTokenizerTest;


public class Controller {
	
	@FXML
	private TextArea editor;
	
	@FXML
	private TableView<ObservableMap.Entry<String, Integer>> registersTable;
	
	@FXML
	private TableColumn<ObservableMap.Entry<String, Integer>,String> registerNameCol;
	@FXML 
	private TableColumn<ObservableMap.Entry<String, Integer>,String> registerValueCol;
	
	private ArmProgram program;
	
	private ObservableMap<String, Integer> registers;
	
	@FXML
	private void initialize() {
        

	}
	
	@FXML
	public void step() {
		System.out.println("step");
		program.step(registers);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@FXML
	public void startDebug() {
		InputStream is = ArmSourceTokenizerTest.class.getResourceAsStream("add.s");
		program = new ArmSourceCompiler().compile(is);
		registers = FXCollections.observableHashMap();
		
		registers.put("R0", 1);
		registers.put("R1", 2);
		
		registersTable.setEditable(true);
		
		registers.addListener((MapChangeListener.Change<? extends String, ? extends Integer> change) -> {
			registersTable.getItems().setAll(registers.entrySet());
		});
		
		registerNameCol.setCellValueFactory((p) -> {
		    return new SimpleStringProperty(p.getValue().getKey());
		});
		
		registerNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		registerNameCol.setOnEditCommit((TableColumn.CellEditEvent<Map.Entry<String,Integer>, String> t) -> {
//		    final String oldKey = t.getOldValue();
//		    final Integer oldPrice = registers.get(oldKey);
//		    registers.remove(oldKey);
//		    registers.put(t.getNewValue(),oldPrice);
//		});
		
		registerValueCol.setCellValueFactory((p) -> {
		    return new SimpleStringProperty(p.getValue().getValue().toString());
		});
		
		registerValueCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		registerValueCol.setOnEditCommit((TableColumn.CellEditEvent<Map.Entry<String,Integer>, String> t) -> {
//			registers.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getKey(),//key
//		           t.getNewValue());//val);
//		});
		registersTable.getItems().setAll(registers.entrySet());

	}
	
}
