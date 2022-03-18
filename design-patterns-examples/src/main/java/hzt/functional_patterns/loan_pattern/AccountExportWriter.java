package hzt.functional_patterns.loan_pattern;

import org.hzt.test.TestSampleGenerator;
import org.jooq.lambda.Unchecked;

import java.io.IOException;
import java.io.Writer;

public class AccountExportWriter {

    public void writeAccounts(Writer writer) throws IOException {
        writer.write("AccountNr;Balance\n");
        TestSampleGenerator.createSampleBankAccountList().stream()
                .map(o -> o.getAccountNumber() + ";" + o.getBalance() + System.lineSeparator())
                .forEach(Unchecked.consumer(writer::write));
    }
}
