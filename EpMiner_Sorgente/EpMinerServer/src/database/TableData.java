package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import database.TableSchema.Column;

/**
 * @author mapTutor
 * Modella lo schema di una tabella nel database relazionale
 */
public class TableData {

    private Connection connection;

    public TableData(Connection connection) {
        this.connection = connection;
    }

    public class TupleData {
        public List<Object> tuple = new ArrayList<Object>();

        public String toString() {
            String value = "";
            Iterator<Object> it = tuple.iterator();
            while (it.hasNext())
                value += (it.next().toString() + " ");

            return value;
        }
    }


    /**
     * <p>Ricava lo schema della tabella con nome passato come parametro. Esegue una interrogazione per estrarre le tuble da tale tabella.
     * Per ogni tupla del runSet. Per la tupla corrente nel resultSet vengono estratti i valori dei singoli campi.</p>
     *
     * @param table nome della tabella del database
     * @return Lista di tuple memorizzate nella tabella
     * @throws SQLException In caso di errore nella connessione con Database relazionale.
     */
    public List<TupleData> getTransazioni(String table) throws SQLException {
        LinkedList<TupleData> transSet = new LinkedList<TupleData>();
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);


        String query = "select ";

        for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
            Column c = tSchema.getColumn(i);
            if (i > 0)
                query += ",";
            query += c.getColumnName();
        }
        if (tSchema.getNumberOfAttributes() == 0)
            throw new SQLException();
        query += (" FROM " + table);

        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            TupleData currentTuple = new TupleData();
            for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
                if (tSchema.getColumn(i).isNumber())
                    currentTuple.tuple.add(rs.getFloat(i + 1));
                else
                    currentTuple.tuple.add(rs.getString(i + 1));
            transSet.add(currentTuple);
        }
        rs.close();
        statement.close();


        return transSet;

    }

    /**
     * Formula ed esegue una interrogazione SQL per estrarre i valori distinti, ordinati di <b>column</b> e popolare una lista da restituire
     *
     * @param table  Nome della tabella
     * @param column nome della colonna della tabella
     * @return Lista di valori distinti in modalità <U>ascendente</U> che l'attributo identificato da nome <b>column</b> assume nella tabella identificata dal nome <b>table</b>.
     * @throws SQLException In caso di errore nella connessione con Database relazionale.
     */
    public List<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        LinkedList<Object> valueSet = new LinkedList<Object>();
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);


        String query = "select distinct ";

        query += column.getColumnName();

        query += (" FROM " + table);

        query += (" ORDER BY " + column.getColumnName());


        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (column.isNumber())
                valueSet.add(rs.getFloat(1));
            else
                valueSet.add(rs.getString(1));

        }
        rs.close();
        statement.close();

        return valueSet;

    }

    /**
     * <p>Formula ed esegue una interrogazione SQL per estrarre il valore aggregato (valore minimo o valore massimo) cercato nella colonna di nome <b>Column</b> della tabella
     * di nome <b>table</b></p>
     *
     * @param table     nome della tabella.
     * @param column    nome della colonna.
     * @param aggregate operatore SQL di aggregazione (min,max).
     * @return
     * @throws SQLException     In caso di errore nella connessione con Database relazionale.
     * @throws NoValueException Tale metodo solleva tale eccezione quando il resultSet è vuoto o il valore calcolato è pari a null.
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
        Statement statement;
        TableSchema tSchema = new TableSchema(table, connection);
        Object value = null;
        String aggregateOp = "";

        String query = "select ";
        if (aggregate == QUERY_TYPE.MAX)
            aggregateOp += "max";
        else
            aggregateOp += "min";
        query += aggregateOp + "(" + column.getColumnName() + ") FROM " + table;


        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (column.isNumber())
                value = rs.getFloat(1);
            else
                value = rs.getString(1);

        }
        rs.close();
        statement.close();
        if (value == null)
            throw new NoValueException("No " + aggregateOp + " on " + column.getColumnName());

        return value;

    }


}
