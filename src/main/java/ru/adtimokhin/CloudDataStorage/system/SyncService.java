package ru.adtimokhin.CloudDataStorage.system;

/**
 * На данный момент, это - заготовка под работу с таймером.
 * Этот интерфейс позволяет синхронизировать локальную папку с удаленным репозиторием
 */
public interface SyncService {
    boolean status();

    void sync();

    boolean start();

    boolean stop();
}
