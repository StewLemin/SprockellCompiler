package ut.pp.compiler.codegen;

public class Spril {

        public static String compute(String operator, String reg1, String reg2, String resReg) {
            return "Compute " + operator + " " + reg1 + " " + reg2 + " " + resReg;
        }

        public static String jump(String target) {
            return "Jump " + target;
        }

        public static String branch(String reg, String target) {
            return "Branch " + reg + " " + target;
        }

        public static String load(String addrImmDI, String reg) {
            return "Load " + addrImmDI + " " + reg;
        }

        public static String store(String reg, String addrImmDI) {
            return "Store " + reg + " " + addrImmDI;
        }

        public static String push(String reg) {
            return "Push " + reg;
        }

        public static String pop(String reg) {
            return "Pop " + reg;
        }

        public static String readInstr(String addrImmDI) {
            return "ReadInstr " + addrImmDI;
        }

        public static String receive(String reg) {
            return "Receive " + reg;
        }

        public static String writeInstr(String reg, String addrImmDI) {
            return "WriteInstr " + reg + " " + addrImmDI;
        }

        public static String testAndSet(String addrImmDI) {
            return "TestAndSet " + addrImmDI;
        }

        public static String endProg() {
            return "EndProg";
        }

        public static String nop() {
            return "Nop";
        }

        public static String debug(String message) {
            return message;
        }

        public static String immValue(int n) {
            return "(ImmValue " + n + ")";
        }

        public static String dirAddr(int addr) {
            return "(DirAddr " + addr + ")";
        }

        public static String indAddr(String reg) {
            return "(IndAddr " + reg + ")";
        }

        public static String abs(int codeAddr) {
            return "(Abs " + codeAddr + ")";
        }

        public static String rel(int offset) {
            return "(Rel " + offset + ")";
        }

        public static String ind(String reg) {
            return "(Ind " + reg + ")";
        }
}
