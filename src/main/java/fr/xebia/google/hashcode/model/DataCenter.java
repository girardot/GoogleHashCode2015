package fr.xebia.google.hashcode.model;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {

    private List<Row> rows = new ArrayList<>();

    private List<Server> servers = new ArrayList<>();

    public List<Row> getRows() {
        return rows;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

    public void addServer(Server server) {
        this.servers.add(server);
    }

    public void addServers(List<Server> servers) {
        this.servers = servers;
    }

    public void setUnavailableAt(int rowIndex, int location) {
        Row row = rows.get(rowIndex);
        row.locations[location] = State.NOT_AVAILABLE;
    }
}
