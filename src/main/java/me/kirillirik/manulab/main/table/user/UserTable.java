package me.kirillirik.manulab.main.table.user;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.kirillirik.User;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.auth.Role;
import me.kirillirik.manulab.main.Editor;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.Table;
import me.kirillirik.util.PasswordUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserTable extends Table<UserRow> {

    public UserTable() {
        super(TableType.USER, 5);
    }

    @Override
    protected UserRow newRow() {
        return new UserRow(-1, "Введите логин", "Выберите роль", -1, true);
    }

    @Override
    protected UserRow getRow(ResultSet rs) throws SQLException {
        final UserRow row = new UserRow(rs.getInt("id"),
                rs.getString("login"),
                rs.getString("role"),
                rs.getInt("collector_id"), false);

        final User user = Auth.user();
        if (user.isNeedUpdate() && row.getID() == user.getID()) {
            user.setLogin(row.getLogin());
            user.setRole(row.getRole());
            user.setCollectorID(row.getCollectorID());

            Auth.clearSession();
            Editor.setState(Editor.State.EMPTY);

            user.setNeedUpdate(false);
        }

        return row;
    }

    @Override
    protected void removeRow(UserRow row) {
        Database.sync().update("delete from \"user\" where id = ?", row.getID());
    }

    @Override
    protected void initTableConfig() {
        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Логин");
        ImGui.tableSetupColumn("Новый пароль");
        ImGui.tableSetupColumn("Роль");
        ImGui.tableSetupColumn("ID сборщика");
    }

    @Override
    protected void addRow(int index, boolean canEdit, UserRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        ImGui.tableSetColumnIndex(2);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputText("##login",  row.login())) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(3);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());

        final ImString newPassword = new ImString();
        if (ImGui.inputText("##newPassword",  newPassword, canEdit ? ImGuiInputTextFlags.Password : ImGuiInputTextFlags.ReadOnly)) {

            row.newSalt().set(PasswordUtil.generateSalt());
            row.newPassword().set(PasswordUtil.hashPassword(newPassword.get(), row.newSalt().toString()));

            row.needUpdatePassword();
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(4);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());

        final String userRole = row.getRole().toUpperCase();
        if (canEdit) {
            if (ImGui.beginCombo("##role", userRole)) {
                for (final var type : Role.VALUES) {
                    if (userRole.equals(type.name())) {
                        continue;
                    }

                    if (ImGui.selectable(type.name())) {
                        row.role().set(type.name());

                        row.dirty();

                        dirty();
                    }
                }

                ImGui.endCombo();
            }
        } else {
            ImGui.text(userRole);
        }

        ImGui.tableSetColumnIndex(5);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputInt("##collector_id",  row.collectorID(), canEdit ? ImGuiInputTextFlags.Password : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        if (row.isDirty() && row.getID() == Auth.user().getID()) {
            Auth.user().setNeedUpdate(true);
        }
    }

    @Override
    protected void updateRow(UserRow row) {
        if (row.isNeedUpdatePassword()) {
            if (row.getNewPassword().isEmpty() || row.getNewSalt().isEmpty()) {
                //TODO maybe give error?
                return;
            }

            Database.sync().update("update \"user\" set login = ?, password = ?, salt = ?, role = ?, collector_id = ? where id = ?",
                    row.getLogin(), row.getNewPassword(), row.getNewSalt(), row.getRole(), row.getCollectorID(), row.getID());
            return;
        }

        Database.sync().update("update \"user\" set login = ?, role = ?, collector_id = ? where id = ?",
                row.getLogin(), row.getRole(), row.getCollectorID(), row.getID());
    }

    @Override
    protected void insertRow(UserRow row) {
        if (row.getNewPassword().isEmpty() || row.getNewSalt().isEmpty()) {
            //TODO maybe give error?
            return;
        }

        Database.sync().update("insert into \"user\"(login, password, salt, role, collector_id) values(?, ?, ?, ?, ?)",
                row.getLogin(), row.getNewPassword(), row.getNewSalt(), row.getRole(), row.getCollectorID());
    }

    @Override
    protected String selectWhat() {
        return "id, login, role, collector_id";
    }
}
