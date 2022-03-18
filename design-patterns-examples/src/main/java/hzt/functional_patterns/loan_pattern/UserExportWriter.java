package hzt.functional_patterns.loan_pattern;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.jooq.lambda.Unchecked;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class UserExportWriter {

    protected void writeUsers(Writer writer) throws IOException {
        writer.write("Id;name");
        TestSampleGenerator.createSampleBankAccountList().stream()
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(c -> c.getId() + ";" + c.getName() + System.lineSeparator())
                .forEach(Unchecked.consumer(writer::write));
    }
}
