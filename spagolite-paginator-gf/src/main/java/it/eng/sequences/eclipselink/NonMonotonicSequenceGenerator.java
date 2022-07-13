package it.eng.sequences.eclipselink;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Vector;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.NativeSequence;
import org.eclipse.persistence.sessions.Session;

/**
 *
 * @author Quaranta_M
 */
public class NonMonotonicSequenceGenerator extends NativeSequence implements SessionCustomizer {

    private static SecureRandom rand = new SecureRandom();

    private static final int MAX = 9999;
    private static final int MIN = 1000;

    public NonMonotonicSequenceGenerator() {
    }

    public NonMonotonicSequenceGenerator(boolean shouldUseIdentityIfPlatformSupports) {
        super(shouldUseIdentityIfPlatformSupports);
    }

    public NonMonotonicSequenceGenerator(String name) {
        super(name);
    }

    public NonMonotonicSequenceGenerator(String name, boolean shouldUseIdentityIfPlatformSupports) {
        super(name, shouldUseIdentityIfPlatformSupports);
    }

    public NonMonotonicSequenceGenerator(String name, int size) {
        super(name, size);
    }

    public NonMonotonicSequenceGenerator(String name, int size, boolean shouldUseIdentityIfPlatformSupports) {
        super(name, size, shouldUseIdentityIfPlatformSupports);
    }

    public NonMonotonicSequenceGenerator(String name, int size, int initialValue) {
        super(name, size, initialValue);
    }

    public NonMonotonicSequenceGenerator(String name, int size, int initialValue,
            boolean shouldUseIdentityIfPlatformSupports) {
        super(name, size, initialValue, shouldUseIdentityIfPlatformSupports);
    }

    @Override
    public Object getGeneratedValue(Accessor accessor, AbstractSession writeSession, String seqName) {
        if (shouldUsePreallocation()) {
            return null;
        } else {
            Number value = updateAndSelectSequence(accessor, writeSession, seqName, 1);
            if (value == null) {
                throw DatabaseException.errorPreallocatingSequenceNumbers();
            }
            long newval = Long.parseLong(randInt() + "" + value);
            return newval;
        }
    }

    @Override
    public Vector getGeneratedVector(Accessor accessor, AbstractSession writeSession, String seqName, int size) {
        if (shouldUsePreallocation()) {
            Number value = updateAndSelectSequence(accessor, writeSession, seqName, size);
            if (value == null) {
                throw DatabaseException.errorPreallocatingSequenceNumbers();
            }
            long newval = Long.parseLong(randInt() + "" + value);
            return createVector(newval, seqName, size);
        } else {
            return null;
        }
    }

    @Override
    public void customize(Session session) {

        Map<Class, ClassDescriptor> entities = session.getDescriptors();
        for (Map.Entry<Class, ClassDescriptor> entry : entities.entrySet()) {
            NonMonotonicSequenceGenerator sequence = new NonMonotonicSequenceGenerator(
                    entry.getValue().getSequenceNumberName(), 1);
            session.getLogin().addSequence(sequence);
        }
    }

    /**
     * Returns a pseudo-random number between MIN and MAX, inclusive. The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @return Integer between min and max, inclusive.
     * 
     * @see java.util.Random#nextInt(int)
     */
    private static int randInt() {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((MAX - MIN) + 1) + MIN;

    }

}
