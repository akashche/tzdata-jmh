/*
 * Copyright 2015 Red Hat, Inc.
 *
 * This file is part of tzdbgen.
 *
 * tzdbgen is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * tzdbgen is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with tzdbgen; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */

package com.redhat.openjdk.tzdata;

import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark class
 */

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TzDataBenchmark {

    @Setup(Level.Trial)
    public void prepare() {
        File projectDir = codeSourceDir(TzDataBenchmark.class).getParentFile();
        System.setProperty("zoneinfo.java.home", new File(projectDir, "src/test/resources").getAbsolutePath());
        System.setProperty("tzdb.java.home", new File(projectDir, "src/test/resources").getAbsolutePath());
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 0)
    @Measurement(iterations = 1)
    @Fork(value = 1000)
    @OperationsPerInvocation(value = 1)
    public zoneinfo.java.util.TimeZone measureZoneInfoGetOneDublin() {
//        System.out.println(zoneinfo.java.util.TimeZone.getTimeZone("Europe/Dublin"));
        return zoneinfo.java.util.TimeZone.getTimeZone("Europe/Dublin");
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 0)
    @Measurement(iterations = 1)
    @Fork(value = 1000)
    @OperationsPerInvocation(value = 1)
    public tzdb.java.util.TimeZone measureTzDbGetOneDublin() {
        tzdb.sun.util.calendar.ZoneInfoFile.fireLoad();
//        System.out.println(tzdb.java.util.TimeZone.getTimeZone("Europe/Dublin"));
        return tzdb.java.util.TimeZone.getTimeZone("Europe/Dublin");
    }


    private static File codeSourceDir(Class<?> clazz) {
        try {
            URI uri = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
            File jarOrDir = new File(uri);
            return jarOrDir.isDirectory() ? jarOrDir : jarOrDir.getParentFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
